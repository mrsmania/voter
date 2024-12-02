package zhaw.voter.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import zhaw.voter.model.Option;
import zhaw.voter.repository.OptionRepository;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Option findOption(long optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new EntityNotFoundException("Option with id " + optionId + " not found"));
    }
}
