package apps.master.repair.http

import apps.master.repair.model.EditPasswordDto
import apps.master.repair.model.InventoryDto
import apps.master.repair.model.LoginDto
import apps.master.repair.model.LogoutDto
import apps.master.repair.model.ServiceDto
import apps.master.repair.uitl.ApiHttpUrl
import com.google.gson.reflect.TypeToken
import nearby.lib.netwrok.response.CorHttp
import nearby.lib.netwrok.response.InfoResponse
import nearby.lib.netwrok.response.ResponseHolder

class RepoImpl : Repo {


    override suspend fun login(params: MutableMap<String, Any>): ResponseHolder<LoginDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.login,
            params = params,
            type = object : TypeToken<InfoResponse<LoginDto>>() {}.type
        )
    }


    override suspend fun logout(params: MutableMap<String, Any>): ResponseHolder<LogoutDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.logout,
            params = params,
            type = object : TypeToken<InfoResponse<LogoutDto>>() {}.type
        )
    }


    override suspend fun change(params: MutableMap<String, Any>): ResponseHolder<String> {
        return CorHttp.getInstance().post(
            url = "${ApiHttpUrl.change}",
            params = params,
            type = object : TypeToken<InfoResponse<EditPasswordDto>>() {}.type
        )
    }

    override suspend fun staff(params: MutableMap<String, Any>,  id: String ): ResponseHolder<MutableList<ServiceDto>> {
        return CorHttp.getInstance().get(
            url = "${ApiHttpUrl.staff}${id}",
            params = params,
            type = object : TypeToken<InfoResponse<MutableList<ServiceDto>>>() {}.type
        )
    }

    override suspend fun staffWork(params: MutableMap<String, Any>,  id: String): ResponseHolder<InventoryDto> {
        return CorHttp.getInstance().get(
            url = "${ApiHttpUrl.staffWork}${id}",
            params = params,
            type = object : TypeToken<InfoResponse<InventoryDto>>() {}.type
        )
    }

    override suspend fun upload(
        params: MutableMap<String, Any>
    ): ResponseHolder<String> {
        return CorHttp.getInstance().postMultipart(
            url = ApiHttpUrl.upload,
            headers = null,
            params = params,
            type = object : TypeToken<InfoResponse<String>>() {}.type
        )
    }
}