package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository; //SpringDataJPA가 구현체를 만든거
    @Autowired TeamRepository teamRepository; //SpringDataJPA가 구현체를 만든거

    @Test
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("userA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> byId = memberRepository.findById(savedMember.getId());
        Member findMember = byId.get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장 같은 인스턴스라고 보장됨.

    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //카운드 조회 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);
        /*
        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
         */
    }
    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void testNamedQuery() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(memberA);
    }
    @Test
    public void testQuery() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findUser("AAA",10);//레파지토리에 바로 JPQL넣은거 이 기능을 많이 씀
        assertThat(result.get(0)).isEqualTo(memberA);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }
    
    //DTO조회
    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    //재미있는기능 파라미터바인딩 //재미있는기능 컬렉션 파라미터 바인딩 in절로 여러개를 조회하고 싶을떄 쓰는방식
    @Test
    public void findByNames() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNmaes(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
    //반환 타입
    @Test
    public void returnType () {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA"); //컬렉션으로 받기 //없는값으로 조회를 할경우 빈 컬렉션을 제공해준다. 절대 null이 아니다.
        Member findMember = memberRepository.findMemberByUsername("AAA"); //단건으로 받기 //없으면 null로 나온다.
        Optional<Member> findOptionalMember = memberRepository.findOptionalByUsername("AAA");//결과가 2건 이상: javax.persistence.NonUniqueResultException 예외 발생
        System.out.println("findMember = " + findMember);
    }
    //페이징 테스트
    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username"); //0페이지에서 3개가져와 //username을desc로

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent(); //0번쨰 페이지 3개 데이터가 꺼내진다. //조회된 데이터
        long totalElements = page.getTotalElements(); //토탈카운트

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3); ; //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0);//페이지번호
        assertThat(page.getTotalPages()).isEqualTo(2); //총 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번쨰 페이지인지
        assertThat(page.hasNext()).isTrue(); //다음페이지가 있는지
    }




}