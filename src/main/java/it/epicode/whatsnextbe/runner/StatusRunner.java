package it.epicode.whatsnextbe.runner;

import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StatusRunner implements ApplicationRunner {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!statusRepository.existsByStatus(Status.StatusType.COMPLETATO)) {
            statusRepository.save(new Status(Status.StatusType.COMPLETATO));
            System.out.println("Status: " + Status.StatusType.COMPLETATO + "caricato con successo");
        } else {
            System.out.println("Status: " + Status.StatusType.COMPLETATO + " gia' caricato");
        }
        if (!statusRepository.existsByStatus(Status.StatusType.NON_COMPLETATO)) {
            statusRepository.save(new Status(Status.StatusType.NON_COMPLETATO));
            System.out.println("Status: " + Status.StatusType.NON_COMPLETATO + "caricato con successo");
        } else {
            System.out.println("Status: " + Status.StatusType.NON_COMPLETATO + " gia' caricato");
        }
        if (!statusRepository.existsByStatus(Status.StatusType.IN_CORSO)) {
            statusRepository.save(new Status(Status.StatusType.IN_CORSO));
            System.out.println("Status: " + Status.StatusType.IN_CORSO + "caricato con successo");
        } else {
            System.out.println("Status: " + Status.StatusType.IN_CORSO + " gia' caricato");
        }

    }
}
