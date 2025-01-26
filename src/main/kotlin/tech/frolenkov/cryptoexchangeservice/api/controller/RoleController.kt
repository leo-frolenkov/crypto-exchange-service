package tech.frolenkov.cryptoexchangeservice.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.frolenkov.cryptoexchangeservice.api.controller.RoleController.Companion.uri
import tech.frolenkov.cryptoexchangeservice.entity.Role
import tech.frolenkov.cryptoexchangeservice.service.user.RoleService

@Tag(name = "Role")
@RestController
@RequestMapping(uri)
class RoleController(
    private val service: RoleService
) {

    @Operation(summary = "Create role")
    @PostMapping
    fun save(@RequestBody role: Role) =
        service.createRole(role)

    @Operation(summary = "find roles start with, param is empty return all role")
    @GetMapping
    fun findRoleStartWith(@RequestParam("name") name: String?) =
        service.findByNameStartWith(name)

    @Operation(summary = "Find role by id")
    @GetMapping("{id}")
    fun findById(@PathVariable id: Long) =
        service.findById(id)


    companion object {
        const val uri = "/api/v1/roles"
    }
}