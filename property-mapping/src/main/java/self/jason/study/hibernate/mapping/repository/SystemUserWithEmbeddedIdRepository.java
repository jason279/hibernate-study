package self.jason.study.hibernate.mapping.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import self.jason.study.hibernate.mapping.entity.SystemUserWithEmbeddedId;

public interface SystemUserWithEmbeddedIdRepository extends CrudRepository<SystemUserWithEmbeddedId, Serializable> {

}
