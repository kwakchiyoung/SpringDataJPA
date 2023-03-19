package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findTop3By();
    @Query(name = "Member.findByUsername") //NamedQuery
    List<Member> findByUsername(@Param("username") String username); //JPQL로 명확한 :떙떙 을 지정해줬을떈 넣어줘야함. 이번에는 NamedQuery를 사용하였기에.
    
    
    //이 기능을 많이 씀 이건 어플리케이션 실행시 오타가 나면 바로 잡아줌 , 이름이 없는 NamedQuery 정적 쿼리이기떄매 문법오류를 잡아줌
    @Query("select m from Member m where m.username = :username and m.age = :age") //JPQL을 바로 넣기 //이기능을 많이씀
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    //맴버의 하나의 컬럼만 조회하는방법
    @Query("select m.username from Member m")
    List<String> findUsernameList();
    //DTO로 조회하는방법
    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t") //JPQL은 이렇게 조인한다. JPQL이 제공하는 new operation
    List<MemberDto> findMemberDto();
    //재미있는기능 컬렉션 파라미터 바인딩 in절로 여러개를 조회하고 싶을떄 쓰는방식
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNmaes(@Param("names") Collection<String> names);

}
