package br.com.redes2.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.redes2.api.model.Transacao;
import br.com.redes2.api.repository.TransacaoRepository;

/**
 * CRUD de transações financeiras.
 * Roda na VM 2 (192.168.1.100:8080).
 * Requisições chegam via proxy reverso Nginx da VM 1 (192.168.1.10:80).
 *
 * Rotas expostas:
 *   GET    /transacoes       → lista todas as transações
 *   POST   /transacoes       → cadastra nova transação
 *   GET    /transacoes/{id}  → busca transação por ID
 *   PUT    /transacoes/{id}  → atualiza transação existente
 *   DELETE /transacoes/{id}  → remove transação
 */
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    // GET /transacoes → SELECT * FROM transacoes
    @GetMapping
    public List<Transacao> listar() {
        return transacaoRepository.findAll();
    }

    // POST /transacoes → INSERT INTO transacoes
    // Retorna 201 com o objeto criado (ID gerado pelo banco).
    @PostMapping
    public ResponseEntity<Transacao> cadastrar(@RequestBody Transacao transacao) {
        Transacao novaTransacao = transacaoRepository.save(transacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
    }

    // GET /transacoes/{id} → SELECT * FROM transacoes WHERE id = ?
    // Retorna 404 se o ID não existir.
    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        Optional<Transacao> transacao = transacaoRepository.findById(id);

        if (transacao.isPresent()) {
            return ResponseEntity.ok(transacao.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // PUT /transacoes/{id} → UPDATE transacoes SET ... WHERE id = ?
    // Sobrescreve todos os campos da transação existente.
    // Retorna 404 se o ID não existir.
    @PutMapping("/{id}")
    public ResponseEntity<Transacao> atualizar(@PathVariable Long id, @RequestBody Transacao transacaoAtualizada) {
        Optional<Transacao> transacao = transacaoRepository.findById(id);

        if (transacao.isPresent()) {
            Transacao existente = transacao.get();
            existente.setDescricao(transacaoAtualizada.getDescricao());
            existente.setValor(transacaoAtualizada.getValor());
            existente.setData(transacaoAtualizada.getData());
            existente.setTipo(transacaoAtualizada.getTipo());
            Transacao salva = transacaoRepository.save(existente);
            return ResponseEntity.ok(salva);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // DELETE /transacoes/{id} → DELETE FROM transacoes WHERE id = ?
    // Retorna 204 se deletou, 404 se não encontrou.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Transacao> transacao = transacaoRepository.findById(id);

        if (transacao.isPresent()) {
            transacaoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
