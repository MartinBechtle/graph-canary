const fakeGraph = {
    nodes: [
        {
            name: 'api-1',
            type: 'API'
        },
        {
            name: 'api-2',
            type: 'API'
        },
        {
            name: 'api-3',
            type: 'API'
        },
        {
            name: 'db-1',
            type: 'DATABASE'
        },
        {
            name: 'db-2',
            type: 'DATABASE'
        },
        {
            name: 'cache-1',
            type: 'CACHE'
        },
        {
            name: 'storage-1',
            type: 'STORAGE'
        },
        {
            name: 'config-1',
            type: 'CONFIGURATION'
        },
        {
            name: 'worker-1',
            type: 'WORKER'
        },
        {
            name: 'worker-2',
            type: 'WORKER'
        },
        {
            name: 'ftp-1',
            type: 'FTP'
        },
        {
            name: 'queue-1',
            type: 'MESSAGE_QUEUE'
        },
        {
            name: 'channel-1',
            type: 'MESSAGE_CHANNEL'
        },
        {
            name: 'http-1',
            type: 'HTTP_RESOURCE'
        },
        {
            name: 'resource-1',
            type: 'RESOURCE'
        }
    ],
    edges: [
        {
            from: 'api-1',
            to: 'api-2'
        },
        {
            from: 'api-1',
            to: 'api-3'
        },
        {
            from: 'api-1',
            to: 'db-1'
        },
        {
            from: 'api-2',
            to: 'api-3'
        },
        {
            from: 'api-2',
            to: 'db-2'
        },
        {
            from: 'api-3',
            to: 'cache-1'
        },
        {
            from: 'api-3',
            to: 'storage-1'
        },
        {
            from: 'api-1',
            to: 'config-1'
        },
        {
            from: 'api-1',
            to: 'worker-1'
        },
        {
            from: 'api-2',
            to: 'worker-2'
        },
        {
            from: 'api-1',
            to: 'ftp-1'
        },
        {
            from: 'api-1',
            to: 'queue-1'
        },
        {
            from: 'api-2',
            to: 'queue-1'
        },
        {
            from: 'api-3',
            to: 'queue-1'
        },
        {
            from: 'api-3',
            to: 'channel-1'
        },
        {
            from: 'worker-1',
            to: 'channel-1'
        },
        {
            from: 'worker-2',
            to: 'stream-1'
        },
        {
            from: 'api-1',
            to: 'http-1'
        },
        {
            from: 'api-3',
            to: 'resource-1'
        }
    ]
};