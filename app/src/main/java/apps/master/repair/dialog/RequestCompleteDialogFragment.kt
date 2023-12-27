package apps.master.repair.dialog

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import apps.master.repair.R
import apps.master.repair.adapter.ItemShowAlbumAdapter
import apps.master.repair.adapter.ItemAddAlbumAdapter
import apps.master.repair.databinding.FragmentRequestCompleteBinding
import apps.master.repair.model.AlbumDto
import apps.master.repair.uitl.ConstantUtil
import apps.master.repair.uitl.ConstantUtil.ALBUM_REQUEST_CODE
import com.bumptech.glide.Glide
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import nearby.lib.base.dialog.BaseBindDialogFragment
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.uikit.recyclerview.BaseRecyclerAdapter
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx
import java.io.File

/*維修完成提交*/
class RequestCompleteDialogFragment : BaseBindDialogFragment<FragmentRequestCompleteBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_request_complete
    }

    private val itemAlbumAdapter by lazy {
        ItemAddAlbumAdapter(
            requireActivity(),
            list,
            mMaxNumber
        )
    }
    private var list = mutableListOf<String>()
    private var albumDtos = mutableListOf<AlbumDto>()
    private val itemAlbumAdapter2 by lazy { ItemShowAlbumAdapter() }
    private var mMaxNumber: Int = 5

    //1.維修地點照片 2.維修位置照片
    var repType: Int = ConstantUtil.ALBUM_LEAFLET
    override fun initialize(view: View, savedInstanceState: Bundle?) {
        binding.iconBack.setOnClickListener { dismiss() }

        //維修地點
//        binding.addressEt
        //維修詳情
//        binding.detailEt

        //維修地點照片
//        binding.ivRepLoc1

//        binding.addressText
        //維修位置照片


        binding.recycle.adapter = itemAlbumAdapter
        binding.recycle.layoutManager = GridLayoutManager(context, 3)
        binding.recycle.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null

        itemAlbumAdapter.setOnItemClickListener(object :
            apps.master.repair.adapter.ItemClickListener {
            override fun onItemClick(obj: Any, position: Int) {
                repType = ConstantUtil.ALBUM_MULTIPLE
                Log.e("position", "position---->$position")
                if (list.size === position && mMaxNumber - list.size > 0) {
                    //动态申请权限
                    val size = mMaxNumber - list.size
                    goAlbum(size)
                } else {
                    //跳转至删除或者预览页面
                }
            }
        })
        itemAlbumAdapter.setOnDeleteClickListener(object :
            apps.master.repair.adapter.DeleteListener {
            override fun onDeleteClick(obj: Any, position: Int) {
                removeList(position)
            }
        })


        //提交上傳
        binding.confirm.setOnClickListener {
            ToastUtils.showToast("提交")
        }

        albumDtos.add(AlbumDto("水喉渠務", R.drawable.img_del))
        albumDtos.add(AlbumDto("防漏防水", R.drawable.img_del))
        albumDtos.add(AlbumDto("門窗", R.drawable.img_del))
        albumDtos.add(AlbumDto("木工", R.drawable.img_del))
        albumDtos.add(AlbumDto("廢物處理", R.drawable.img_del))
        albumDtos.add(AlbumDto("冷氣", R.drawable.img_del))
        itemAlbumAdapter2.setItems(albumDtos)
        binding.recycle2.adapter = itemAlbumAdapter2
        binding.recycle2.layoutManager = GridLayoutManager(context, 3)
        binding.recycle2.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycle2.setHasFixedSize(true)
        binding.recycle2.itemAnimator = null
        itemAlbumAdapter2.setOnItemClickListener(listener = object :
            BaseRecyclerAdapter.OnItemClickListener<AlbumDto> {
            override fun onItemClick(holder: Any, item: AlbumDto, position: Int) {
            }
        })
    }


    /**
     * 删除图片
     *
     * @param position
     */
    private fun removeList(position: Int) {
        list.removeAt(position)
        itemAlbumAdapter.nodfiyData(list)
    }

    private fun goAlbum(selectable: Int) {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .countable(true)
            .maxSelectable(selectable)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(false) // Default is `true`
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "PhotoPicker"))
            .forResult(ALBUM_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ALBUM_REQUEST_CODE -> {
                data?.let {
                    when (repType) {
                        ConstantUtil.ALBUM_LEAFLET -> {
                            val photoPath = Matisse.obtainPathResult(it)[0]
                            photoPath?.let {
                                val f = File(it)
                                Glide.with(this).load(f).into(binding.ivRepLoc1)
                            }
                        }

                        ConstantUtil.ALBUM_MULTIPLE -> {
                            val pathList = Matisse.obtainPathResult(it)
                            for (i in 0 until pathList.size) {
                                val path = pathList[i]
                                println("图片${(i + 1)} 地址 $path")
                                list.add(path)
                            }
                            itemAlbumAdapter.nodfiyData(list)
                        }

                        else -> {}
                    }
                }

            }
        }
    }
}