package net.thumbtack.school.hospital.TestDataBase;

import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestPatientDao {

    @Autowired
    private PatientDao patientDao;

    @Before()
    public void clearDatabase() {
        patientDao.deleteAll();
    }

    @Test
    public void TestInsertPatient() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        patient1 = patientDao.insert(patient1);

        Patient actualPatient = patientDao.getById(patient1.getId());

        assertEquals(patient1, actualPatient);
    }

    @Test
    public void TestInsertPatients() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        User user2 = new User(UserType.PATIENT,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Patient patient2 = new Patient(user2, "TonyStar@mail.ru", "Space Street 213", "89043241284");

        User user3 = new User(UserType.PATIENT,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Patient patient3 = new Patient(user3, "PPots78@mail.ru", "Baker Street 221 B", "89043245781");

        patient1 = patientDao.insert(patient1);
        patient2 = patientDao.insert(patient2);
        patient3 = patientDao.insert(patient3);

        List<Patient> patientsExpected = new ArrayList<>();
        patientsExpected.add(patient1);
        patientsExpected.add(patient2);
        patientsExpected.add(patient3);

        List<Patient> patientsActual = patientDao.getAll();

        Assert.assertTrue(patientsActual.containsAll(patientsExpected));
        Assert.assertTrue(patientsExpected.containsAll(patientsActual));
    }


    @Test
    public void TestUpdatePatient() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        patient1 = patientDao.insert(patient1);

        patient1.getUser().setLastName("Stane Lee");
        patient1.getUser().setLogin("DeadWood55");
        patient1.setAddress("8 - Volgogradskaya 74");
        patient1.setEmail("ObaStaneVano13@mail.ru");

        patient1 = patientDao.update(patient1);

        Patient actualPatient = patientDao.getById(patient1.getId());

        assertEquals(patient1, actualPatient);
    }

    @Test
    public void TestUpdatePatients() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        User user2 = new User(UserType.PATIENT,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Patient patient2 = new Patient(user2, "TonyStar@mail.ru", "Space Street 213", "89043241284");

        User user3 = new User(UserType.PATIENT,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Patient patient3 = new Patient(user3, "PPots78@mail.ru", "Baker Street 221 B", "89043245781");

        patient1 = patientDao.insert(patient1);
        patient2 = patientDao.insert(patient2);
        patient3 = patientDao.insert(patient3);

        patient1.getUser().setLastName("Stane Lee");
        patient1.getUser().setLogin("DeadWood55");
        patient1.setAddress("8 - Volgogradskaya 73");
        patient1.setEmail("ObaStaneVano13@mail.ru");

        patient2.getUser().setLastName("Space");
        patient2.setEmail("AntonyStarkIM@mail.ru");

        patient3.setAddress("Forest Avenu 17");
        patient3.setEmail("PaperPots78@mail.ru");

        patient1 = patientDao.update(patient1);
        patient2 = patientDao.update(patient2);
        patient3 = patientDao.update(patient3);

        List<Patient> patientsExpected = new ArrayList<>();
        patientsExpected.add(patient1);
        patientsExpected.add(patient2);
        patientsExpected.add(patient3);

        List<Patient> patientsActual = patientDao.getAll();

        Assert.assertTrue(patientsActual.containsAll(patientsExpected));
        Assert.assertTrue(patientsExpected.containsAll(patientsActual));
    }

    @Test
    public void TestDeletePatient() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        patient1 = patientDao.insert(patient1);

        patientDao.delete(patient1.getId());

        List<Patient> patientsActual = patientDao.getAll();

        assertEquals(patientsActual.size(), 0);
        assertFalse(patientsActual.contains(patient1));
    }

    @Test
    public void TestDeleteTwoPatients() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        User user2 = new User(UserType.PATIENT,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Patient patient2 = new Patient(user2, "TonyStar@mail.ru", "Space Street 213", "89043241284");

        User user3 = new User(UserType.PATIENT,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Patient patient3 = new Patient(user3, "PPots78@mail.ru", "Baker Street 221 B", "89043245781");

        patient1 = patientDao.insert(patient1);
        patient2 = patientDao.insert(patient2);
        patient3 = patientDao.insert(patient3);

        patientDao.delete(patient2.getId());
        patientDao.delete(patient3.getId());

        List<Patient> patientsExpected = new ArrayList<>();
        patientsExpected.add(patient1);


        List<Patient> patientsActual = patientDao.getAll();

        Assert.assertTrue(patientsActual.containsAll(patientsExpected));
        Assert.assertTrue(patientsExpected.containsAll(patientsActual));
        assertFalse(patientsActual.contains(patient2));
        assertFalse(patientsActual.contains(patient3));
        assertEquals(patientsActual.size(), 1);
    }

    @Test
    public void TestDeleteAllPatients() {
        User user1 = new User(UserType.PATIENT,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Patient patient1 = new Patient(user1, "ObaStane13@mail.ru", "Prospect Mira 67 b", "89043245588");

        User user2 = new User(UserType.PATIENT,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Patient patient2 = new Patient(user2, "TonyStar@mail.ru", "Space Street 213", "89043241284");

        User user3 = new User(UserType.PATIENT,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Patient patient3 = new Patient(user3, "PPots78@mail.ru", "Baker Street 221 B", "89043245781");

        patient1 = patientDao.insert(patient1);
        patient2 = patientDao.insert(patient2);
        patient3 = patientDao.insert(patient3);

        patientDao.delete(patient1.getId());
        patientDao.delete(patient2.getId());
        patientDao.delete(patient3.getId());


        List<Patient> patientsActual = patientDao.getAll();

        assertFalse(patientsActual.contains(patient1));
        assertFalse(patientsActual.contains(patient2));
        assertFalse(patientsActual.contains(patient3));
        assertEquals(patientsActual.size(), 0);
    }

}
