package com.example.firestore

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firestore.adapters.UserAdapter
import com.example.firestore.databinding.ActivityMainBinding
import com.example.firestore.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {

    lateinit var firebaseFirestore: FirebaseFirestore

    lateinit var binding: ActivityMainBinding

    lateinit var userAdapter: UserAdapter

    lateinit var list: ArrayList<User>

    //Storage

    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference

    var imgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Store

        firebaseFirestore = FirebaseFirestore.getInstance()

        list = ArrayList()


        //Storage
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images")


        //Write
        binding.button.setOnClickListener {

            val name = binding.editOne.text.toString()
            val age = binding.editTwo.text.toString()

            val user = User(name, age, imgUrl)

            firebaseFirestore.collection("Artikov").add(user).addOnSuccessListener {
                Toast.makeText(this, it.id, Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        }


        //Read
        firebaseFirestore.collection("Artikov").get().addOnCompleteListener {
            if (it.isSuccessful) {

                val result = it.result

                result?.forEach { queryDocumentSnapshot ->

                    val user = queryDocumentSnapshot.toObject(User::class.java)

                    list.add(user)

                }

                userAdapter = UserAdapter(list)
                binding.rv.adapter = userAdapter

            }
        }


        binding.imageId.setOnClickListener {

            getImageContent.launch("image/*")

        }

        //Permission for Galery
        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()


    }

    //Galery
    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            binding.imageId.setImageURI(uri)
            val m = System.currentTimeMillis()
            val uploadTask = reference.child(m.toString()).putFile(uri!!)

            //val uploadTask = reference.putFile(uri!!)

            uploadTask.addOnSuccessListener {

                if (it.task.isSuccessful) {

                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imgUri ->

                        imgUrl = imgUri.toString()


                    }

                }

            }.addOnFailureListener {

            }

        }

}