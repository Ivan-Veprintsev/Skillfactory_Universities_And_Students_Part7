import comparators.StudentComparator;
import comparators.UniversityComparator;
import enums.StudentComparatorType;
import enums.UniversityComparatorType;
import objects.FullInfo;
import objects.Statistics;
import objects.Student;
import objects.University;
import util.ComparatorUtil;
import util.JsonUtil;
import util.StatisticsUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        logger.log(INFO, "Application started");

        List<University> universities = XSSFReader.readUniversities();
        UniversityComparator universityComparator = ComparatorUtil.getUniversityComparator(UniversityComparatorType.ID);
        universities.sort(universityComparator);

        List<Student> students = XSSFReader.readStudents();
        StudentComparator studentComparator = ComparatorUtil.getStudentComparator(StudentComparatorType.FULL_NAME);
        students.sort(studentComparator);

        List<Statistics> statisticsList = StatisticsUtil.createStatistics(students, universities);
        XSSFWriter.writeXlsStatistics(statisticsList, "src\\main\\resources\\statistics.xlsx");

        FullInfo fullInfo = new FullInfo()
                .setStudentList(students)
                .setUniversityList(universities)
                .setStatisticsList(statisticsList)
                .setProcessDate(new Date());

        XmlWriter.generateXmlReq(fullInfo);
        JsonWriter.writeJsonReq(fullInfo);

        logger.log(INFO, "Application finished");
    }

}
