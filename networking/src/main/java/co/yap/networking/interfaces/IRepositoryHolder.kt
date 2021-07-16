package co.yap.networking.interfaces

import co.yap.networking.BaseRepository

interface IRepositoryHolder<T: BaseRepository> {
    val repository: T
}