package ch.zhaw.psit4.martin.api;

import reactor.fn.Consumer;
import reactor.bus.Event;
import ch.zhaw.psit4.martin.api.types.MEventData;

public abstract class MEventListener implements Consumer<Event<MEventData>> {

    @Override
    public void accept(Event<MEventData> event) {
        this.handleEvent(event.getData());
    }

    public abstract void handleEvent(MEventData event);
}
