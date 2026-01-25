package dev.esbi.mizan.mvikotlin.utils

import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import java.lang.ref.WeakReference

/**
 * Binds a Store to a LifecycleOwner and ViewModelStoreOwner.
 *
 * The logic is necessary for saving the Store when the configuration changes.
 * The initState parameter initializes the Store to a state one level higher so
 * that labels can subscribe earlier than the Store is started.
 * If null is passed as the Lifecycle.State parameter, the Store must be initialized
 * manually if auto-initialization of the Store is disabled.
 *
 * @param T the type of the LifecycleOwner and ViewModelStoreOwner.
 * @param V the type of the Store.
 * @param key the key to retain the Store instance.
 * @param initState the initial state of the Lifecycle for the Store (default is CREATED).
 * @param block a lambda function that provides the Store instance.
 * @return a Lazy property delegate that lazily initializes the Store.
 * @throws IllegalArgumentException if the initState is not null and is not at least CREATED state.
 */
inline fun <T, reified V : Store<*, *, *>> T.bindStore(
    key: String = V::class.java.name,
    initState: Lifecycle.State? = Lifecycle.State.CREATED,
    noinline block: () -> V
): Lazy<V> where T : LifecycleOwner, T : ViewModelStoreOwner = StoreLazy(
    initState = initState,
    key = key,
    block = block,
    target = this
)

@PublishedApi
internal class StoreLazy<T, V : Store<*, *, *>>(
    target: T,
    private val key: String,
    private val initState: Lifecycle.State?,
    private val block: () -> V
) : DefaultLifecycleObserver, Lazy<V> where T : LifecycleOwner, T : ViewModelStoreOwner {
    private var store: V? = null
    private var targetReference: WeakReference<T>? = WeakReference(target)

    /**
     * Gets the initialized Store instance.
     * If the Store is not initialized, it will be initialized.
     *
     * @return the initialized Store instance.
     */
    override val value: V
        get() = initialize()

    init {
        require(initState == null || initState.isAtLeast(Lifecycle.State.CREATED)) {
            "Store cannot initialize with the ${initState?.name} lifecycle state"
        }
        target.lifecycle.addObserver(this)
    }

    /**
     * Checks whether the Store is initialized.
     *
     * @return true if the Store is initialized, false otherwise.
     */
    override fun isInitialized(): Boolean = store != null

    /**
     * Initializes the Store if it's not already initialized.
     */
    @MainThread
    override fun onCreate(owner: LifecycleOwner) {
        if (store == null) {
            initialize()
        }
        if (initState == null) {
            owner.lifecycle.removeObserver(this)
        }
        targetReference?.clear()
        targetReference = null
    }

    /**
     * Called when the LifecycleOwner is started.
     * Initializes the Store if initState is CREATED.
     */
    override fun onStart(owner: LifecycleOwner) {
        if (initState == Lifecycle.State.CREATED) {
            init(owner)
        }
    }

    /**
     * Called when the LifecycleOwner is resumed.
     * Initializes the Store if initState is at least STARTED.
     */
    override fun onResume(owner: LifecycleOwner) {
        if (initState?.isAtLeast(Lifecycle.State.STARTED) == true) {
            init(owner)
        }
    }

    /**
     * Initializes the Store and removes the observer from the LifecycleOwner.
     */
    private fun init(owner: LifecycleOwner) {
        value.init()
        owner.lifecycle.removeObserver(this)
    }

    /**
     * Initializes the Store if it's not already initialized, and returns the
     * initialized Store instance.
     *
     * @return Store instance.
     */
    @Suppress("UNCHECKED_CAST")
    @MainThread
    private fun initialize(): V {
        store?.let { return it }
        val instanceKeeper = targetReference?.get()?.instanceKeeper()
        store = instanceKeeper?.getStore<Store<*, *, *>>(key, block) as V
        return requireNotNull(store)
    }
}
