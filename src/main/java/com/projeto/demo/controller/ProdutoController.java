package com.projeto.demo.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.demo.model.Produtos;
import com.projeto.demo.repository.CategoriaRepository;
import com.projeto.demo.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Produtos>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produtos> getById(@PathVariable Long id) {
		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produtos>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));

	}

	@PostMapping
	public ResponseEntity<Produtos> post(@Valid @RequestBody Produtos produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
	}

	@PutMapping
	public ResponseEntity<Produtos> put(@Valid @RequestBody Produtos produto) {
		if (produtoRepository.existsById(produto.getId())) {
			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produtos> produto = produtoRepository.findById(id);

		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		produtoRepository.deleteById(id);
	}

}