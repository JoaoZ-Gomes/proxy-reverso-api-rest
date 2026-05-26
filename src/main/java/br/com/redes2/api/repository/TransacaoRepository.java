package br.com.redes2.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.redes2.api.model.Transacao;

/**
 * Acesso à tabela "transacoes" no banco neondb (Neon Cloud).
 * CRUD completo herdado do JpaRepository — usado pelo TransacaoController.
 */
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
