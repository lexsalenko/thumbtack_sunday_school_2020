package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.*;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.CommonService;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.validators.AdminValidator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


/*@Configuration
@MapperScan("net.thumbtack.school.hospital.mappers")*/
/*@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
@MapperScan("net.thumbtack.school.hospital")*/
@Configuration
@PropertySource("classpath:application.properties")
@MapperScan("net.thumbtack.school.hospital.mappers")
public class Config {

    @Bean("service1")
    public AdminService separateService1(AdminDaoImpl adminDao, UserDaoImpl userDao, CommonDaoImpl commonDao) {
        return new AdminService(adminDao, userDao, commonDao);
    }

    @Bean("service2")
    public DoctorService separateService2(DoctorDaoImpl doctorDao, CommonDaoImpl commonDao, PatientDaoImpl patientDao, UserDaoImpl userDao) {
        return new DoctorService(doctorDao, commonDao, patientDao, userDao);
    }

    @Bean("service3")
    public PatientService separateService3(PatientDaoImpl patientDao, CommonDaoImpl commonDao, UserDaoImpl userDao, DoctorDaoImpl doctorDao) {
        return new PatientService(patientDao, commonDao, userDao, doctorDao);
    }

    @Bean("service4")
    public CommonService separateService4(CommonDaoImpl commonDao, UserDaoImpl userDao, AdminDaoImpl adminDao, PatientDaoImpl patientDao, DoctorDaoImpl doctorDao) {
        return new CommonService(commonDao, userDao, adminDao, patientDao, doctorDao);
    }

    @Bean
    public UserDaoImpl getUserDaoImpl() {
        return new UserDaoImpl();
    }

    @Bean
    public CommonDaoImpl getCommonDaoImpl() {
        return new CommonDaoImpl();
    }

    @Bean
    public PatientDaoImpl getPatientDaoImpl() {
        return new PatientDaoImpl();
    }

    @Bean
    public DoctorDaoImpl getDoctorDaoImpl() {
        return new DoctorDaoImpl();
    }

    @Bean
    public AdminDaoImpl getAdminDaoImpl() {
        return new AdminDaoImpl();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        ds.setUrl("jdbc:mysql://localhost:3306/hospital?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Omsk");
        ds.setUsername("test");
        ds.setPassword("test");
        return ds;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }


}
