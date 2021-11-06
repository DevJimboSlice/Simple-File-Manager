package dev.jimboslice.filemanager.providers

import android.database.Cursor
import android.database.MatrixCursor
import android.icu.util.ULocale.ROOT
import android.os.Build
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import dev.jimboslice.filemanager.R

class DocsProvider: DocumentsProvider() {
    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun queryRoots(p0: Array<out String>?): Cursor {
        // Use a MatrixCursor to build a cursor
        // with either the requested fields, or the default
        // projection if "projection" is null.
        val result = MatrixCursor(resolveRootProjection(projection))

        // If user is not logged in, return an empty root cursor.  This removes our
        // provider from the list entirely.
        if (!isUserLoggedIn()) {
            return result
        }

        // It's possible to have multiple roots (e.g. for multiple accounts in the
        // same app) -- just add multiple cursor rows.
        result.newRow().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                add(DocumentsContract.Root.COLUMN_ROOT_ID, ROOT)
            }

            // You can provide an optional summary, which helps distinguish roots
            // with the same title. You can also use this field for displaying an
            // user account name.
            add(DocumentsContract.Root.COLUMN_SUMMARY, context?.getString(R.string.root_summary))

            // FLAG_SUPPORTS_CREATE means at least one directory under the root supports
            // creating documents. FLAG_SUPPORTS_RECENTS means your application's most
            // recently used documents will show up in the "Recents" category.
            // FLAG_SUPPORTS_SEARCH allows users to search all documents the application
            // shares.
            add(
                DocumentsContract.Root.COLUMN_FLAGS,
                DocumentsContract.Root.FLAG_SUPPORTS_CREATE or
                    DocumentsContract.Root.FLAG_SUPPORTS_RECENTS or
                    DocumentsContract.Root.FLAG_SUPPORTS_SEARCH
            )

            // COLUMN_TITLE is the root title (e.g. Gallery, Drive).
            add(DocumentsContract.Root.COLUMN_TITLE, context?.getString(R.string.title))

            // This document id cannot change after it's shared.
            add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, getDocIdForFile(baseDir))

            // The child MIME types are used to filter the roots and only present to the
            // user those roots that contain the desired type somewhere in their file hierarchy.
            add(DocumentsContract.Root.COLUMN_MIME_TYPES, getChildMimeTypes(baseDir))
            add(DocumentsContract.Root.COLUMN_AVAILABLE_BYTES, baseDir.freeSpace)
            add(DocumentsContract.Root.COLUMN_ICON, R.drawable.ic_launcher)
        }

        return result
    }

    override fun queryDocument(p0: String?, p1: Array<out String>?): Cursor {
        TODO("Not yet implemented")
    }

    override fun queryChildDocuments(p0: String?, p1: Array<out String>?, p2: String?): Cursor {
        TODO("Not yet implemented")
    }

    override fun openDocument(p0: String?, p1: String?, p2: CancellationSignal?): ParcelFileDescriptor {
        TODO("Not yet implemented")
    }
}
