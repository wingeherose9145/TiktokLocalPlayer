package com.example.tiktokplayer

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
                    1
                )
            } else {
                loadVideos()
            }
        } else {
            loadVideos()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        loadVideos()
    }

    private fun loadVideos() {

        val videoList = mutableListOf<Uri>()

        val projection = arrayOf(MediaStore.Video.Media._ID)

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)

                val contentUri = Uri.withAppendedPath(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )

                videoList.add(contentUri)
            }
        }

        viewPager.adapter = VideoAdapter(this, videoList)
    }
}