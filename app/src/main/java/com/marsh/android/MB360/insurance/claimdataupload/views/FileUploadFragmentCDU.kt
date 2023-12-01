package com.marsh.android.MB360.insurance.claimdataupload.views

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.marsh.android.MB360.R
import com.marsh.android.MB360.databinding.FragmentFileUploadCDUBinding
import com.marsh.android.MB360.insurance.claimdataupload.adapters.ClaimFileUploadAdapter
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.FileUploadListerner
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.marsh.android.MB360.insurance.claimdataupload.responseclass.ClaimFileUpload
import com.marsh.android.MB360.utilities.FileUtil
import java.io.File
import java.util.Objects


class FileUploadFragmentCDU : Fragment(), FileUploadListerner {
    private var viewPagerNavigation: ViewPagerNavigation? = null

    var position: Int? = null
    var adapter: ClaimFileUploadAdapter? = null
    var fileLauncherActivity: ActivityResultLauncher<Intent>? = null
    private val binding get() = _binding!!
    private var _binding: FragmentFileUploadCDUBinding? = null
    private var responseList = ArrayList<ClaimFileUpload>()
    private var fileDataList = ArrayList<String>()

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewPagerNavigation = it.getSerializable("viewPagerNavigation") as? ViewPagerNavigation
        }



        fileLauncherActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //queryViewModel.setLoadingFromFilePicker()

            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data = result.data
                Log.d("FILE-PICKER", "filepicker: " + result.data!!.type)

                try {

                    val file2 = FileUtil.from(requireContext(), data!!.data)
                    // File file2 = new File(getPath(data.getData()));
                    val filePath = file2.path
                    var FileExists = false
                    val filenameArray = filePath.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val extension = filenameArray[filenameArray.size - 1]

                    val fileName = getFileNameFromPath(filePath)

                    val file = File(filePath)
                    val file_size: String = java.lang.String.valueOf(file.length() / 1024).toInt().toString()

                    if (file_size.toInt() <= 5000) {

                        if (!fileDataList.contains(fileName)) {
                            responseList[position!!].fileName = fileName
                            responseList[position!!].status = true
                            responseList[position!!].fileSize = file_size

                            if ((position!! + 1) == responseList.size) {
                                val response4 = ClaimFileUpload(
                                        items = "Additional document",
                                        int = position!! + 1,
                                        status = false,
                                        fileSize = "",
                                        fileName = "",
                                        mandatory = false,
                                        data = null
                                )
                                responseList.add(response4)
                            }

                            adapter!!.notifyItemChanged(position!!)
                            adapter?.notifyDataSetChanged()
                            fileName.let { fileDataList.add(fileName) }

                        } else {
                            Toast.makeText(requireContext(), "File already uploaded", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(requireContext(), "File max size is 5mb", Toast.LENGTH_SHORT).show()
                    }
                    validate()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    }

    fun getFileNameFromPath(filePath: String): String {
        val lastIndex = filePath.lastIndexOf('/')
        if (lastIndex != -1) {
            return filePath.substring(lastIndex + 1)
        }
        return filePath // Return the entire string if no slash is found
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFileUploadCDUBinding.inflate(inflater, container, false)

        val view = binding.root

        val response1 = ClaimFileUpload("Birth certificate", 1, false, "", "", mandatory = true, data = null)
        val response2 = ClaimFileUpload("Marriage certificate", 2, false, "", "", mandatory = true, data = null)
        val response3 = ClaimFileUpload("Additional document", 3, false, "", "", mandatory = false, data = null)


        responseList.add(response1)
        responseList.add(response2)
        responseList.add(response3)

        adapter = ClaimFileUploadAdapter(responseList, this)
        binding.rvFileUpload.adapter = adapter

        return view
    }


    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation): FileUploadFragmentCDU {
            val fragment = FileUploadFragmentCDU()
            val args = Bundle()
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
            return fragment
        }
    }

    override fun fileOnClickListerner(pos: Int) {
        // openFilePicker()
        position = pos
        val item = responseList[position!!]
        if (item.status) {
            removeData()
        } else {
            val filePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            filePickerIntent.type = "*/*" // Allow all file types
            fileLauncherActivity?.launch(filePickerIntent)
            this.position = pos
        }


    }

    private fun removeData() {
        /* val builder = AlertDialog.Builder(context)
         builder.setTitle("Delete File")
         builder.setMessage("Are you sure want to delete this file ?")
         builder.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_alert))

         //performing positive action
         builder.setPositiveButton("Yes") { dialogInterface, which ->


         }
         //performing cancel action
         builder.setNeutralButton("Cancel") { dialogInterface, which ->

         }
         //performing negative action
         builder.setNegativeButton("No") { dialogInterface, which ->

         }

         val alertDialog: AlertDialog = builder.create()
         alertDialog.setCancelable(false)
         alertDialog.show()*/


        //e-card available
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.smslayout)
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder))

        val lblSMS = dialog.findViewById<AppCompatTextView>(R.id.lblSMS)
        val smsContact = dialog.findViewById<AppCompatEditText>(R.id.smsContact)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnSubmit)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
        val llTitle = dialog.findViewById<LinearLayout>(R.id.llTitle)
        val lblSMSHeader = dialog.findViewById<AppCompatTextView>(R.id.lblSMSHeader)
        llTitle.visibility = View.VISIBLE
        lblSMS.text = "Alert"
        lblSMSHeader.text = "Are you sure you want to delete this file?"
        btnSubmit.text = "Yes"
        btnCancel.text = "No"
        smsContact.visibility = View.GONE
        btnSubmit.setOnClickListener { v1: View? ->

            fileDataList.remove(responseList[position!!].fileName)
            if (responseList[position!!].mandatory) {
                responseList[position!!].status = false
                responseList[position!!].fileName = ""
                responseList[position!!].fileSize = ""
                responseList[position!!].data = null
            } else {
                responseList.removeAt(position!!)
            }
            adapter?.notifyDataSetChanged()
            validate()
            dialog.dismiss()

        }
        btnCancel.setOnClickListener { v: View? ->
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        fileLauncherActivity = registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // queryViewModel.setLoadingFromFilePicker()
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                Log.d("FILE-PICKER", "filepicker: " + result.data!!.type)
                try {
                    val file2 = FileUtil.from(context, data!!.data)
                    // File file2 = new File(getPath(data.getData()));
                    val filePath = file2.path
                    var FileExists = false
                    val filenameArray = filePath.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val extension = filenameArray[filenameArray.size - 1]

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    private fun validate() {
        val allMandatoryFileUploaded = responseList.none { it.mandatory && !it.status }
        if (allMandatoryFileUploaded) {
            viewPagerNavigation?.enableNextLayout()
        } else {
            viewPagerNavigation?.disableNextLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        validate()
    }

}





