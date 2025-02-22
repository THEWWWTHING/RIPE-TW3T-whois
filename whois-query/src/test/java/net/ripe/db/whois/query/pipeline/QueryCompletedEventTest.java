package net.ripe.db.whois.query.pipeline;

import io.netty.channel.Channel;
import net.ripe.db.whois.query.domain.QueryCompletionInfo;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class QueryCompletedEventTest {
    @Mock private Channel channel;

    private QueryCompletedEvent subject;

    @Test
    public void no_completioninfo() {
        subject = new QueryCompletedEvent(channel);

        assertThat(subject.getChannel(), is(channel));
        assertThat(subject.getFuture().channel(), is(channel));
        assertThat(subject.isForceClose(), is(false));
        assertNull(subject.getCompletionInfo());
        assertThat(subject.toString(), containsString("null"));
    }

    @Test
    public void completioninfo_no_force_close() {
        final QueryCompletionInfo completionInfo = QueryCompletionInfo.PARAMETER_ERROR;
        subject = new QueryCompletedEvent(channel, completionInfo);

        assertThat(subject.getCompletionInfo().isForceClose(), is(false));
        assertThat(subject.isForceClose(), is(false));
        assertThat(subject.getCompletionInfo(), is(completionInfo));
        assertThat(subject.toString(), containsString(completionInfo.name()));
    }

    @Test
    public void completioninfo_force_close() {
        final QueryCompletionInfo completionInfo = QueryCompletionInfo.EXCEPTION;
        subject = new QueryCompletedEvent(channel, completionInfo);

        assertThat(subject.getCompletionInfo().isForceClose(), is(true));
        assertThat(subject.isForceClose(), is(true));
        assertThat(subject.getCompletionInfo(), is(completionInfo));
    }

    @Test
    public void equals_completionInfo() {
        final QueryCompletionInfo completionInfo = QueryCompletionInfo.EXCEPTION;
        subject = new QueryCompletedEvent(channel, completionInfo);

        assertFalse(subject.equals(null));
        assertFalse(subject.equals(""));
        assertTrue(subject.equals(subject));

        final QueryCompletedEvent queryCompletedEvent = new QueryCompletedEvent(channel, completionInfo);
        assertTrue(subject.equals(queryCompletedEvent));
        assertThat(subject.hashCode(), is(queryCompletedEvent.hashCode()));

        assertFalse(subject.equals(new QueryCompletedEvent(Mockito.mock(Channel.class), completionInfo)));
        assertFalse(subject.equals(new QueryCompletedEvent(channel, QueryCompletionInfo.PARAMETER_ERROR)));
    }

    @Test
    public void equals_no_completionInfo() {
        subject = new QueryCompletedEvent(channel);

        assertFalse(subject.equals(null));
        assertFalse(subject.equals(""));
        assertTrue(subject.equals(subject));

        final QueryCompletedEvent queryCompletedEvent = new QueryCompletedEvent(channel);
        assertTrue(subject.equals(queryCompletedEvent));
        assertThat(subject.hashCode(), is(queryCompletedEvent.hashCode()));

        assertFalse(subject.equals(new QueryCompletedEvent(Mockito.mock(Channel.class))));
    }
}
