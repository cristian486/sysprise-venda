package br.com.sysprise.venda.repository;


import br.com.sysprise.venda.model.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    Page<Venda> findAllByHabilitadoTrue(Pageable pageable);
    List<Venda> findAllByClienteId(Long id);
}
