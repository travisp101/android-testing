package com.example.android.testing.notes.notes

import com.example.android.testing.notes.data.Note
import com.example.android.testing.notes.data.NotesRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Created by travis on 3/13/2017 AD.
 */
object NotesPresenterSpec : Spek({
    given("a NotesPresenter") {
        val notesRepository: NotesRepository = mock()
        val notesView: NotesContract.View = mock()
        val notesPresenter: NotesPresenter = NotesPresenter(notesRepository, notesView)
        val callbackCaptor = argumentCaptor<NotesRepository.LoadNotesCallback>()
        val NOTES = listOf(Note("Title1", "Description1"), Note("Title2", "Description2"))

        afterEachTest {
            reset(notesRepository)
            reset(notesView)
        }

        on("load notes") {
            notesPresenter.loadNotes(true)

            it("should load notes from the repository") {
                verify(notesRepository).getNotes(callbackCaptor.capture())
                callbackCaptor.lastValue.onNotesLoaded(NOTES)
            }

            it("should show and hide a progress in order") {
                inOrder(notesView) {
                    verify(notesView).setProgressIndicator(true)
                    verify(notesView).setProgressIndicator(false)
                }
            }

            it("should show notes on the view") {
                verify(notesView).showNotes(NOTES)
            }
        }

        on("add a new note") {

            notesPresenter.addNewNote()

            it("should show add note UI") {
                verify(notesView).showAddNote()
            }
        }

        on("open note detail") {

            val requestedNote = Note("Details Requested", "For this note")
            notesPresenter.openNoteDetails(requestedNote)

            it("should show note detail UI") {
                verify(notesView).showNoteDetailUi(any())
            }
        }
    }
})