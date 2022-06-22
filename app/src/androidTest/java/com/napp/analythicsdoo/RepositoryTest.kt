package com.napp.analythicsdoo

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private val repository: Repository = Repository.build()

    @Test
    fun getPullRequest() {
        runBlocking {
            val repositoryList = repository.call(IRepo::class.java) {
                it.getUserRepoList()
            }
//                .filter { repo ->
//                repo.owner.login == "m3can0"
//            }
            assertNotNull(repositoryList)

            repositoryList.forEach { repo ->
                Log.v("getPullRequest()", repo.toString())
                val pullRequestList = getPullRequestList(repo)
                pullRequestList.forEach { pullRequest ->
                    Log.v("getPullRequest()", pullRequest.toString())
                    val commentList = getPullRequestCommentList(repo, pullRequest)
                    commentList.forEach { comment ->
                        Log.v("getPullRequest()", comment.toString())
                    }
                }
            }
        }
    }

    private suspend fun getPullRequestList(repo: Repo) : List<PullRequest>  {
        return repository.call(IPulls::class.java) {
            it.getPullRequestList(repo.owner.login, repo.name)
        }
    }

    private suspend fun getPullRequestCommentList(repo: Repo, pullRequest: PullRequest) : List<PullRequestComment>{
        return repository.call(IPulls::class.java) { pullService ->
            pullService.getPullRequestCommentList(repo.owner.login, repo.name, pullRequest.number)
        }
    }
}