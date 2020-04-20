package br.cache.livros;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import redis.clients.jedis.Jedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/livros")
public class LivroController {
	private List<Livro> livros;
	private Jedis jedis = new Jedis("localhost");
	
	@Autowired
	private LivroRepository repo;
	
	@GetMapping
	public List<Livro> todosOsLivros(){
		return repo.findAll();
	}
	
	@GetMapping("/{id}")
    @Cacheable(cacheNames = Livro.CACHE_NAME, key="#a0")
    public Livro findbyid(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id not found: " + id));
    }
	
	
	@PostMapping(value = "/novo", consumes = MediaType.APPLICATION_JSON_VALUE)
	@CacheEvict(value = Livro.CACHE_NAME, allEntries = true)
	public String postLivro(@RequestBody Livro novo) {		
		novo = repo.save(novo);
		this.jedis.set("Nomedolivro", novo.getTitulo());
		return novo.getId();
	}

	@DeleteMapping("/{id}")
    @CacheEvict(cacheNames = Livro.CACHE_NAME, key="#id")
    public void delete(@PathVariable String id) {
        if(id == null) {
            throw new EntityNotFoundException("id is empty");
        }
        repo.deleteById(id);
    }
}