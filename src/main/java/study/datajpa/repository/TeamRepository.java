package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

@Repository //SpringDataJPA는 이 어노테이션을 생략 가능하다.
public interface TeamRepository extends JpaRepository<Team,Long> {
}
