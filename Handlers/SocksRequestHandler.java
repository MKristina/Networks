package Handlers;

import Models.Connection;
import Socks.SocksRequest;
import Socks.SocksResponse;
import Dns.DnsService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import static Handlers.ConnectHandler.connectToTarget;
import static Socks.SocksParser.parseRequest;

public class SocksRequestHandler extends SocksHandler
{
    private static final byte DOMAIN_NAME_TYPE = 0x03;
    private static final int NO_ERROR = 0;

    public SocksRequestHandler(Connection connection)
    {
        super(connection);
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException
    {
        ByteBuffer outputBuffer = getConnection().getOutputBuffer();
        read(selectionKey);
        SocksRequest request = parseRequest(outputBuffer);

        if (request == null)
        {
            return;
        }

        byte parseError = request.getParseError();

        if(parseError != NO_ERROR)
        {
            onError(selectionKey, parseError);
            return;
        }

        if(request.getAddressType() == DOMAIN_NAME_TYPE)
        {
            DnsService dnsService = DnsService.getInstance();
            dnsService.resolveName(request,selectionKey);
            return;
        }

        connectToTarget(selectionKey, request.getAddress());
    }

    public static void onError(SelectionKey selectionKey, byte error)
    {
        Handler handler = (Handler) selectionKey.attachment();
        Connection connection = handler.getConnection();

        putErrorResponseIntoBuf(selectionKey, connection, error);
        selectionKey.attach(new SocksErrorHandler(connection));
    }

    public static void putErrorResponseIntoBuf(SelectionKey selectionKey, Connection connection,  byte error)
    {
        SocksResponse response = new SocksResponse();
        response.setReply(error);

        ByteBuffer inputBuff = connection.getInputBuffer();
        inputBuff.put(response.toByteBufferWithoutAddress());
        connection.getOutputBuffer().clear();
        selectionKey.interestOpsOr(SelectionKey.OP_WRITE);
    }
}
