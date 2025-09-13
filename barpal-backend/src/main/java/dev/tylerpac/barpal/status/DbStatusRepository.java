package dev.tylerpac.barpal.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbStatusRepository extends JpaRepository<StatusProbeEntity, Long> {
}
