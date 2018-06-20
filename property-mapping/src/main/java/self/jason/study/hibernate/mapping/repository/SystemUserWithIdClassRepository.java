package self.jason.study.hibernate.mapping.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import self.jason.study.hibernate.mapping.entity.SystemUserWithIdClass;

public interface SystemUserWithIdClassRepository extends CrudRepository<SystemUserWithIdClass, Serializable> {

}
