package store.cookshoong.www.cookshoongauth.repository;


import org.springframework.data.repository.CrudRepository;
import store.cookshoong.www.cookshoongauth.entity.RefreshToken;

/**
 * Refresh 토큰을 레디스로부터 가져오기위한 Repository.
 *
 * @author koesnam (추만석)
 * @since 2023.07.19
 */
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
