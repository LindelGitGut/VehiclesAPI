package repositroy;

import com.udacity.boogle.maps.Address;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Address, Long> {
}
