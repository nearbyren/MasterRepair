package apps.master.repair.http


import apps.master.repair.model.InventoryDto
import apps.master.repair.model.LoginDto
import apps.master.repair.model.LogoutDto
import apps.master.repair.model.ServiceDto
import nearby.lib.netwrok.response.ResponseHolder

interface Repo {
    suspend fun login(params: MutableMap<String, Any>): ResponseHolder<LoginDto>
    suspend fun logout(params: MutableMap<String, Any>): ResponseHolder<LogoutDto>
    suspend fun change(params: MutableMap<String, Any>): ResponseHolder<String>
    suspend fun staff(params: MutableMap<String, Any>, id: String): ResponseHolder<MutableList<ServiceDto>>
    suspend fun staffWork(params: MutableMap<String, Any>, id: String): ResponseHolder<InventoryDto>
    suspend fun upload(params: MutableMap<String, Any>): ResponseHolder<String>

}