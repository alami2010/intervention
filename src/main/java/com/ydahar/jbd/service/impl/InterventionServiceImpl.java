package com.ydahar.jbd.service.impl;

import com.lowagie.text.DocumentException;
import com.ydahar.jbd.domain.Intervention;
import com.ydahar.jbd.repository.FloorRepository;
import com.ydahar.jbd.repository.InterventionRepository;
import com.ydahar.jbd.repository.RoomRepository;
import com.ydahar.jbd.repository.TierRepository;
import com.ydahar.jbd.service.InterventionService;
import com.ydahar.jbd.service.MailService;
import com.ydahar.jbd.service.dto.InterventionDTO;
import com.ydahar.jbd.service.mapper.InterventionMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service Implementation for managing {@link Intervention}.
 */
@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

    private final Logger log = LoggerFactory.getLogger(InterventionServiceImpl.class);
    private final SpringTemplateEngine templateEngine;

    private final InterventionRepository interventionRepository;
    private final TierRepository tierRepository;
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;

    private final InterventionMapper interventionMapper;
    private final MailService mailService;

    public InterventionServiceImpl(
        SpringTemplateEngine templateEngine,
        InterventionRepository interventionRepository,
        TierRepository tierRepository,
        RoomRepository roomRepository,
        FloorRepository floorRepository,
        InterventionMapper interventionMapper,
        MailService mailService
    ) {
        this.templateEngine = templateEngine;
        this.interventionRepository = interventionRepository;
        this.tierRepository = tierRepository;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.interventionMapper = interventionMapper;
        this.mailService = mailService;
    }

    @Override
    public InterventionDTO save(InterventionDTO interventionDTO) throws DocumentException, IOException {
        log.debug("Request to save Intervention : {}", interventionDTO);
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention.setCreationDate(LocalDate.now());
        final Intervention interventionSaved = interventionRepository.save(intervention);

        intervention
            .getFloors()
            .forEach(floor -> {
                floor.setIntervention(interventionSaved);
                var floorSaved = floorRepository.save(floor);

                floor
                    .getTiers()
                    .stream()
                    .filter(tier -> tier.isChecked())
                    .forEach(tier -> {
                        tier.setFloor(floorSaved);
                        var tierSaved = tierRepository.save(tier);

                        tier
                            .getRooms()
                            .stream()
                            .filter(room -> room.isChecked())
                            .forEach(room -> {
                                room.setTier(tierSaved);
                                roomRepository.save(room);
                            });
                    });
            });

        if (intervention.getEmail() != null) {
            generatePDFToDownload(interventionRepository.findById(interventionSaved.getId()).orElseThrow(), false);
        }

        return interventionMapper.toDto(interventionSaved);
    }

    @Override
    public InterventionDTO update(InterventionDTO interventionDTO) {
        log.debug("Request to update Intervention : {}", interventionDTO);
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention.setCreationDate(LocalDate.now());
        intervention = interventionRepository.save(intervention);
        return interventionMapper.toDto(intervention);
    }

    @Override
    public Optional<InterventionDTO> partialUpdate(InterventionDTO interventionDTO) {
        log.debug("Request to partially update Intervention : {}", interventionDTO);

        return interventionRepository
            .findById(interventionDTO.getId())
            .map(existingIntervention -> {
                interventionMapper.partialUpdate(existingIntervention, interventionDTO);

                return existingIntervention;
            })
            .map(interventionRepository::save)
            .map(interventionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Interventions");
        return interventionRepository.findAll(pageable).map(interventionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterventionDTO> findOne(Long id) {
        log.debug("Request to get Intervention : {}", id);
        return interventionRepository.findById(id).map(interventionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Intervention : {}", id);
        interventionRepository.deleteById(id);
    }

    @Override
    public File download(Long id) throws DocumentException, IOException {
        Intervention intervention = interventionRepository.findById(id).orElseThrow();

        return generatePDFToDownload(intervention, true);
    }

    private File generatePDFToDownload(Intervention intervention, boolean generate)
        throws DocumentException, IOException, DocumentException {
        Context context = new Context();
        context.setVariable("intervention", intervention);

        if (intervention.getUnitNumber() != null) {
            String utnits = String.join(" and ", intervention.getUnitNumber().split(" "));

            intervention.setUnitNumber(utnits);
        }

        String logoString = Utils.getLogo();

        context.setVariable("logo", logoString);
        context.setVariable("day", intervention.getStart().atZone(ZoneId.systemDefault()).getDayOfWeek());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault());
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(ZoneId.systemDefault());

        context.setVariable("start", formatter.format(intervention.getStart()));
        context.setVariable("finish", formatter.format(intervention.getFinish()));
        context.setVariable("date", formatter2.format(intervention.getStart()));

        String content = templateEngine.process("mail/intervention", context);

        if (intervention.getEmail() != null) {
            mailService.sendEmail(intervention.getEmail(), "New WSD", content, false, true);
        }

        return generate ? Utils.generatePdfFromHtml(content) : null;
    }
}
