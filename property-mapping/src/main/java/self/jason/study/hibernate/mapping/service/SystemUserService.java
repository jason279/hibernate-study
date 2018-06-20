package self.jason.study.hibernate.mapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import self.jason.study.hibernate.mapping.entity.SystemUserWithEmbeddedId;
import self.jason.study.hibernate.mapping.entity.SystemUserWithIdClass;
import self.jason.study.hibernate.mapping.repository.SystemUserWithEmbeddedIdRepository;
import self.jason.study.hibernate.mapping.repository.SystemUserWithIdClassRepository;

@Service
public class SystemUserService {
	@Autowired
	private SystemUserWithEmbeddedIdRepository embeddedIdRepository;
	@Autowired
	private SystemUserWithIdClassRepository idClassRepository;

	@Transactional
	public void add(SystemUserWithEmbeddedId embeddedId, SystemUserWithIdClass idClass) {
		embeddedIdRepository.save(embeddedId);
		idClassRepository.save(idClass);
	}
}
