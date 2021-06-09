package net.thumbtack.school.hospital.TestDataBase;

import net.thumbtack.school.hospital.HospitalServer;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = HospitalServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdminDao {

    @Autowired
    private AdminDao adminDao;


    @Before()
    public void ClearDataBase() {
        adminDao.deleteAll();
    }


    @Test
    public void TestInsertAdmin() {
        User userToDataBase = new User(UserType.ADMIN, "Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin adminToDataBase = new Admin(userToDataBase, "Administrator");

        adminToDataBase = adminDao.insert(adminToDataBase);

        Admin adminFromDataBase = adminDao.getById(adminToDataBase.getId());

        assertEquals(adminFromDataBase, adminToDataBase);
    }


    @Test
    public void TestInsertAdmins() {
        User user1 = new User(UserType.ADMIN,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin adminToDataBase1 = new Admin(user1, "Administrator");

        User user2 = new User(UserType.ADMIN,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Admin adminToDataBase2 = new Admin(user2, "Administrator");

        User user3 = new User(UserType.ADMIN,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Admin adminToDataBase3 = new Admin(user3, "Administrator");

        adminToDataBase1 = adminDao.insert(adminToDataBase1);
        adminToDataBase2 = adminDao.insert(adminToDataBase2);
        adminToDataBase3 = adminDao.insert(adminToDataBase3);

        List<Admin> adminsExpected = new ArrayList<>();
        adminsExpected.add(adminToDataBase1);
        adminsExpected.add(adminToDataBase2);
        adminsExpected.add(adminToDataBase3);

        List<Admin> adminsActual = adminDao.getAll();

        assertTrue(adminsActual.containsAll(adminsExpected));
        assertTrue(adminsExpected.containsAll(adminsActual));
        assertEquals(adminsActual.size(), adminsExpected.size());
    }


    @Test
    public void TestUpdateAdmin() {
        User user1 = new User(UserType.ADMIN,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin admin1 = new Admin(user1, "Administrator");

        admin1 = adminDao.insert(admin1);

        admin1.getUser().setLastName("Stane Lee");
        admin1.getUser().setLogin("DeadWood55");

        admin1 = adminDao.update(admin1);

        Admin actualAdmin = adminDao.getById(admin1.getId());

        assertEquals(admin1, actualAdmin);
    }

    @Test
    public void TestUpdateAdmins() {
        User user1 = new User(UserType.ADMIN,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin admin1 = new Admin(user1, "Administrator");

        User user2 = new User(UserType.ADMIN,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Admin admin2 = new Admin(user2, "Administrator");

        User user3 = new User(UserType.ADMIN,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Admin admin3 = new Admin(user3, "Administrator");

        admin1 = adminDao.insert(admin1);
        admin2 = adminDao.insert(admin2);
        admin3 = adminDao.insert(admin3);

        admin1.getUser().setLastName("Stane Lee");
        admin1.getUser().setLogin("DeadWood55");

        admin2.getUser().setFirstName("Tony");

        admin3.getUser().setPassword("ironpaper.123");

        admin1 = adminDao.update(admin1);
        admin2 = adminDao.update(admin2);
        admin3 = adminDao.update(admin3);

        List<Admin> adminsExpected = new ArrayList<>();
        adminsExpected.add(admin1);
        adminsExpected.add(admin2);
        adminsExpected.add(admin3);

        List<Admin> adminsActual = adminDao.getAll();

        assertTrue(adminsActual.containsAll(adminsExpected));
        assertTrue(adminsExpected.containsAll(adminsActual));
    }

    @Test
    public void TestDeleteAdmin() {
        User user1 = new User(UserType.ADMIN,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin admin1 = new Admin(user1, "Administrator");

        admin1 = adminDao.insert(admin1);

        adminDao.delete(admin1.getId());

        List<Admin> adminsActual = adminDao.getAll();

        assertFalse(adminsActual.contains(admin1));
    }

    @Test
    public void TestDeleteTwoAdmins() {
        User user1 = new User(UserType.ADMIN,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin admin1 = new Admin(user1, "Administrator");

        User user2 = new User(UserType.ADMIN,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Admin admin2 = new Admin(user2, "Administrator");

        User user3 = new User(UserType.ADMIN,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Admin admin3 = new Admin(user3, "Administrator");

        admin1 = adminDao.insert(admin1);
        admin2 = adminDao.insert(admin2);
        admin3 = adminDao.insert(admin3);

        adminDao.delete(admin1.getId());
        adminDao.delete(admin3.getId());

        List<Admin> adminsActual = adminDao.getAll();

        assertFalse(adminsActual.contains(admin1));
        assertFalse(adminsActual.contains(admin3));
        assertTrue(adminsActual.contains(admin2));
    }

    @Test
    public void TestDeleteAllAdmins() {
        User user1 = new User(UserType.ADMIN,"Obadiah", "Stane", "Ivanov", "IronMonger1373", "ironmonger.1234");
        Admin admin1 = new Admin(user1, "Administrator");

        User user2 = new User(UserType.ADMIN,"Antony", "Stark", "Hovard", "IronMan777", "ironman.057");
        Admin admin2 = new Admin(user2, "Administrator");

        User user3 = new User(UserType.ADMIN,"Paper", "Pots", "Harley", "IronWoman", "ironwoman.123");
        Admin admin3 = new Admin(user3, "Administrator");

        admin1 = adminDao.insert(admin1);
        admin2 = adminDao.insert(admin2);
        admin3 = adminDao.insert(admin3);

        adminDao.deleteAll();

        List<Admin> adminsActual = adminDao.getAll();

        assertFalse(adminsActual.contains(admin1));
        assertFalse(adminsActual.contains(admin3));
        assertFalse(adminsActual.contains(admin2));
        assertEquals(0, adminsActual.size());
    }


}
