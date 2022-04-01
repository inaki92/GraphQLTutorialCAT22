package com.example.graphqltutorial.network

import com.apollographql.apollo3.api.ApolloResponse
import com.example.graphqltutorial.InsertUserMutation
import com.example.graphqltutorial.UsersListQuery
import kotlinx.coroutines.flow.Flow

class SpaceRepositoryImpl : SpaceRepository {
    override suspend fun getUsers(limit : Int): ApolloResponse<UsersListQuery.Data> {
        return ApolloService.apollo.query(UsersListQuery(limit)).execute()
    }

    override suspend fun insertUser(user: InsertUserMutation) : Flow<ApolloResponse<InsertUserMutation.Data>> {
        return ApolloService.apollo.mutation(user).toFlow()
    }
}

interface SpaceRepository {
    suspend fun getUsers(limit: Int) : ApolloResponse<UsersListQuery.Data>
    suspend fun insertUser(user: InsertUserMutation) : Flow<ApolloResponse<InsertUserMutation.Data>>
}