package qa.guru;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class HomeworkTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parseZipFile() throws Exception {
        ZipFile zipFile = new ZipFile(
                new File(classLoader.getResource("Files/HomeTask.zip").toURI()));

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();

            try (InputStream stream = zipFile.getInputStream(entry)) {// try with resources
                switch (entry.getName()) {

                    case "junit.pdf": {
                        PDF pdf = new PDF(stream);
                        assertThat(pdf)
                                .containsExactText("JUnit 5 User Guide");
                        System.out.println("В файле " + entry.getName() + " ошибок не найдено");
                        break;
                    }

                    case "sample.xlsx": {
                        XLS xls = new XLS(stream);
                        assertThat(xls.excel
                                .getSheetAt(0)
                                .getRow(2)
                                .getCell(1)
                                .getStringCellValue()).contains("Mara");
                        System.out.println("В файле " + entry.getName() + " ошибок не найдено");

                        break;

                    }

                    case "teacherscsv.csv": {
                        try (CSVReader csv = new CSVReader(
                                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                            List<String[]> content = csv.readAll();
                            assertThat(content.get(0))
                                    .contains("Name", "Surname");
                            assertThat(content.get(1))
                                    .contains("Dmitrii", "Tuchs");
                            assertThat(content.get(2))
                                    .contains("Artem", "Eroshenko");
                            System.out.println("В файле " + entry.getName() + " ошибок не найдено");
                            break;
                        }
                    }
                }
            }
        }
    }
}
