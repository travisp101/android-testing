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
        val repo: NotesRepository = mock()
        val view: NotesContract.View = mock()
        val presenter: NotesPresenter = NotesPresenter(repo, view)
        val captor: KArgumentCaptor<NotesRepository.LoadNotesCallback> = argumentCaptor<NotesRepository.LoadNotesCallback>()
        val NOTES = listOf(Note("Title1", "Description1"), Note("Title2", "Description2"))

        afterEachTest {
            reset(repo)
            reset(view)
        }

        on("load notes") {

            presenter.loadNotes(true)

            it("should load notes from the repo") {
                verify(repo).getNotes(captor.capture())
                captor.lastValue.onNotesLoaded(NOTES)
            }

            it("should show then hide a progress") {
                inOrder(view) {
                    verify(view).setProgressIndicator(true)
                    verify(view).setProgressIndicator(false)
                }
            }

            it("should show notes on the view") {
                verify(view).showNotes(NOTES)
            }
        }

        on("add a new note") {

            presenter.addNewNote()

            it("should show add note UI") {
                verify(view).showAddNote()
            }
        }

        on("open note detail") {

            val requestedNote = Note("Details Requested", "For this note")
            presenter.openNoteDetails(requestedNote)

            it("should show note detail UI") {
                verify(view).showNoteDetailUi(any())
            }
        }
    }
})