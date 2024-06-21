package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (var inputStream = getResourceByFileName(fileNameProvider);
             var reader = new InputStreamReader(inputStream)) {
            var questionDTOs = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withType(QuestionDto.class)
                    .build()
                    .parse();
            return questionDTOsToQuestions(questionDTOs);
        } catch (FileNotFoundException e) {
            throw new QuestionReadException("Not found file with path " + fileNameProvider.getTestFileName(), e);
        } catch (IOException e) {
            throw new QuestionReadException("Can't read resource", e);
        }
    }

    private InputStream getResourceByFileName(TestFileNameProvider fileNameProvider) throws IOException {
        var resource = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
        if (resource == null) {
            throw new IOException("Resource with filename " + fileNameProvider + " is null");
        }
        return resource;
    }

    private List<Question> questionDTOsToQuestions(List<QuestionDto> questionDTOs) {
        return questionDTOs.stream().map(QuestionDto::toDomainObject).toList();
    }
}
