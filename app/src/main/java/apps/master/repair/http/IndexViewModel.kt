package apps.master.repair.http

import androidx.lifecycle.MutableLiveData
import apps.master.repair.model.EditPasswordDto
import apps.master.repair.model.FileSuccessDto
import apps.master.repair.model.InventoryDto
import apps.master.repair.model.LoginDto
import apps.master.repair.model.LogoutDto
import apps.master.repair.model.ServiceDto
import nearby.lib.base.uitl.ToastUtils

import nearby.lib.mvvm.vm.BaseAppViewModel
import java.io.File

/**
 * @author: lr
 * @created on: 2023/12/18 9:14 PM
 * @description:
 */
class IndexViewModel : BaseAppViewModel() {

    private val repo by lazy { RepoImpl() }
    val login = MutableLiveData<LoginDto>()
    var serviceDtos = MutableLiveData<MutableList<ServiceDto>>()
    var inventoryDto = MutableLiveData<InventoryDto>()
    val logoutDto = MutableLiveData<LogoutDto>()

    val fileSuccess = MutableLiveData<FileSuccessDto>()

    val editPasswordDto = MutableLiveData<EditPasswordDto>()

    fun change(id: String, password: String, newPassword: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["id"] = id
            //原密码
            params["password"] = password
            //新密码
            params["newPassword"] = newPassword
            repo.change(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    editPasswordDto.value = EditPasswordDto()
                }.onFailure { _, msg ->
                    editPasswordDto.value = msg?.let { EditPasswordDto(msg = it) }
                }.onCatch {
                    editPasswordDto.value = EditPasswordDto(msg = "服務器異常")
                }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["email"] = email
            params["password"] = password
            repo.login(params)
                .onCompletion {
                    showLoadingView(false)
                }
                .onSuccess {
                    login.value = it
                }.onFailure { _, message ->
                    login.value = LoginDto(message = message)
                }.onCatch {
                    login.value = LoginDto(message = "服務器異常")
                }
        }
    }

    fun logout() {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            repo.logout(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    logoutDto.value = LogoutDto()
                }.onFailure { _, message ->
                    logoutDto.value = message?.let { LogoutDto(msg = it) }
                }.onCatch {
                    logoutDto.value = LogoutDto(msg = "服務器異常")
                }
        }
    }

    fun staff(id: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            repo.staff(params, id).onCompletion { showLoadingView(false) }
                .onSuccess {
                    serviceDtos.value = it
                }.onFailure { _, msg ->
                    msg?.let {
                        ToastUtils.showToast(msg)
                    }
                }.onCatch {
                    ToastUtils.showToast("服務器異常")
                }
        }
    }

    fun staffWork(id: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
//            params["numbers"] = numbers
            repo.staffWork(params, id)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    inventoryDto.value = it
                }.onFailure { _, _ ->
                    serviceDtos.value = mutableListOf()
                }.onCatch {
                    serviceDtos.value = mutableListOf()
                }
        }
    }

    fun upload(file: File) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["file"] = file
            repo.upload(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    fileSuccess.value = FileSuccessDto(path = it)
                }.onFailure { _, message ->
                    fileSuccess.value = FileSuccessDto(msg = message)
                }.onCatch {
                    fileSuccess.value = FileSuccessDto(msg = it.errorMsg)
                }
        }
    }
}