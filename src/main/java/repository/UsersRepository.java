package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{

}
