package br.com.lukemedrano.apiUploadDownload.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.lukemedrano.apiUploadDownload.domain.fileStorage.FileStorageProperties;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/files")
public class FileStorageController {
	private final Path fileStorageLocation;
	
	public FileStorageController(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
				.toAbsolutePath().normalize();
	}
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadArquivo(@RequestParam("arquivo") MultipartFile file){
		String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			Path localAlvoUpload = fileStorageLocation.resolve(nomeArquivo);
			file.transferTo(localAlvoUpload);
			
			String fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/api/files/download/")
					.path(nomeArquivo)
					.toUriString();
			
			return ResponseEntity.ok("Upload feito com sucesso! Link para Download: " + fileDownloadURI);
		} catch(IOException exception) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/download/{nomeArquivo:.+}")
	public ResponseEntity<Resource> downloadArquivo(@PathVariable String nomeArquivo, HttpServletRequest request) throws IOException{
		Path caminhoArquivo = fileStorageLocation.resolve(nomeArquivo);
		
		try {
			Resource recurso = new UrlResource(caminhoArquivo.toUri());
			String tipoConteudo = request.getServletContext().getMimeType(recurso.getFile().getAbsolutePath());
			
			if(tipoConteudo == null) {
				tipoConteudo = "application/octet-stream";
			}
			
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(tipoConteudo))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachament; filename=\"" + recurso.getFilename() + "\"")
					.body(recurso);
		} catch (MalformedURLException exception) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/lista")
	public ResponseEntity<List<String>> listaArquivos() throws IOException{
		List<String> nomesArquivos = Files.list(fileStorageLocation)
				.map(Path::getFileName)
				.map(Path::toString)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(nomesArquivos);
	}
}