package dev.esbi.mizan.feature.newtransaction2.store

/*

internal class AmountInputStoreFactory @Inject constructor(
    private val storeFactory: Provider<StoreFactory>,
    private val amountInputExecutor: Provider<NewTransactionExecutor>,
) {

    fun create(): NewTransactionStore {
        return object : NewTransactionStore,
            Store<NewTransactionStore.Intent, AmountInputState, NewTransactionStore.Label> by
            storeFactory.get().create(
                name = "AmountInputStore",
                initialState = AmountInputState(),
                bootstrapper = SimpleBootstrapper(
                    NewTransactionStore.Action.Init
                ),
                executorFactory = { amountInputExecutor.get() },
                reducer = NewTransactionReducer,
            ) {}
    }
}
*/
