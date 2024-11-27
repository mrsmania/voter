package zhaw.voter.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import zhaw.voter.dto.QuestionDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionService {

    public List<QuestionDTO> verifyAndParseQuestions(MultipartFile file) throws IOException {
        List<QuestionDTO> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each question must have at least two options");
                }
                QuestionDTO question = new QuestionDTO();
                question.setText(values[0]);
                question.setOptions(Arrays.asList(values).subList(1, values.length));
                questions.add(question);
            }
        }

        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file contains no questions");
        }

        return questions;
    }
}
