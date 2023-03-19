package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        //단건조회는 JPA를 쓰면 되는데 전체 조회같은거는 JPQL을 상용해야함. JPQL은 객체를 대상으로 하는 쿼리이다. //JPA기본편수업 다시들어야함.
        List<Member> result = em.createQuery("select m from Member m", Member.class) //Member가 Member엔티티를 말한다. 뒤에는 반환타입을 지정해줌.
                .getResultList();
        return result;
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class) //count는 long타입으로 반환된다.
                .getSingleResult();
    }


    public Member find(Long id) {
        return em.find(Member.class, id);
    }
    public List<Member> findByUsernameAndAgeGreaterThen(String username,int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age >:age")
                .setParameter("username",username)
                .setParameter("age",age)
                .getResultList();
    }
}
