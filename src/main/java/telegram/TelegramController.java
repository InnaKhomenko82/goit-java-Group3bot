package telegram;

import java.io.Serializable;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
public interface TelegramController {
    public <T extends Serializable, Method extends BotApiMethod<T>> T sendMethod(Method method);
}