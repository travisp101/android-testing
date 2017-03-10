package com.example.android.testing.notes.addnote

import com.example.android.testing.notes.data.NotesRepository
import com.example.android.testing.notes.util.ImageFile
import com.natpryce.hamkrest.containsSubstring
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Matchers

/**
 * Specification for the implementation of [AddNotePresenter]
 * Converted from [AddNotePresenterTest]
 */
object AddNotePresenterSpec : Spek({

    given("a NotesPresenter") {

        val notesRepository: NotesRepository = mock()
        val imageFile: ImageFile = mock()
        val addNoteView: AddNoteContract.View = mock()
        val addNotePresenter: AddNotePresenter = AddNotePresenter(notesRepository, addNoteView, imageFile)

        afterEachTest {
            reset(notesRepository)
            reset(imageFile)
            reset(addNoteView)
        }

        on("save note") {
            addNotePresenter.saveNote("New Note Title", "Some Note Description")
            it("should save the note to the repository") {
                verify(notesRepository).saveNote(any())
            }
            it("should show note list") {
                verify(addNoteView).showNotesList()
            }
        }

        on("save empty note") {
            addNotePresenter.saveNote("", "")
            it("should show empty note error") {
                verify(addNoteView).showEmptyNoteError()
            }
        }

        on("take picture") {
            addNotePresenter.takePicture()
            it("should create an image file") {
                verify(imageFile).create(any(), any())
                verify(imageFile).path
            }
            it("should open a camera") {
                verify(addNoteView).openCamera(any())
            }
        }

        on("image available") {
            val imageUrl = "path/to/file"
            whenever(imageFile.exists()).thenReturn(true)
            whenever(imageFile.path).thenReturn(imageUrl)
            addNotePresenter.imageAvailable()
            it("should save image and update the ui with thumbnail") {
                verify(addNoteView).showImagePreview(Matchers.contains(imageUrl))
            }
        }

        on("image available but file does not exist") {
            whenever(imageFile.exists()).thenReturn(false)
            addNotePresenter.imageAvailable()
            it("should show image error") {
                verify(addNoteView).showImageError()
            }
            it("should delete the file") {
                verify(imageFile).delete()
            }
        }

        on("capture failed") {
            addNotePresenter.imageCaptureFailed()
            it("should show image error") {
                verify(addNoteView).showImageError()
            }
            it("should delete the file") {
                verify(imageFile).delete()
            }
        }
    }
})