package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class UploadFileController {

    @Autowired
    lateinit var fileStorage: FileStorage


    @GetMapping("/")
    fun index() : String{
        return "Get ok"
    }

    @PostMapping("/")
    fun uploadMultipartFile(@RequestParam("file") file: MultipartFile, model: Model): String {
        fileStorage.store(file);
        model.addAttribute("message", "File uploaded successfully! -> filename = " + file.getOriginalFilename())
        return "post Ok"
    }

}