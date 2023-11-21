package org.koreait.jwtStudy.repositories;


import org.koreait.jwtStudy.entities.Member;
import org.koreait.jwtStudy.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    Optional<Member> findByEmail(String email);

    //email로 검증
    default boolean exists(String email){
        return exists(QMember.member.email.eq(email));
    }
}
