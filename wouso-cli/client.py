import os
import sys
import ConfigParser
import argparse
from wousoapi import WousoClient

CONFIG_FILE = '/etc/wouso.ini'

def get_config():
    if not os.path.exists(CONFIG_FILE):
        return None
    config = ConfigParser.RawConfigParser()
    config.read(CONFIG_FILE)
    return config


def get_instance(args):
    """
    Return instance configuration, with defaults.
    Args can be:
        - a list of: server port path
        - a string like: server:port/path
    """
    https = False
    server, port, path = 'wouso-next.rosedu.org', 80, ''
    if len(args) == 1:
        if args[0].startswith('http'):
            if args[0].startswith('https'):
                https = True
                port = 443
                args[0] = args[0][8:]
            else:
                args[0] = args[0][7:]
        if ':' in args[0]:
            newargs = args[0].split(':')
            if '/' in newargs[1]:
                newargs = [newargs[0]] + newargs[1].split('/')
            args = newargs
        else:
            if '/' in args[0]:
                a = args[0].split('/')
                args = [a[0], port, a[1]]

    if len(args) > 0:
        server = args[0]
        if len(args) > 1:
            port = int(args[1])
            if len(args) > 2:
                path = args[2]
                if len(args) > 3:
                    https = args[3].lower() == 'https'
    return https, server, port, path


def get_info(options):
    server = options.get('server', 'wouso-next.rosedu.org')
    port = options.get('port', None)
    path = options.get('path', '')
    access_token = options.get('access_token', None)
    https = options.get('https', 'False').lower() == 'true'
    wc = WousoClient(server=server, port=port, path=path, access_token=access_token, https=https)

    info = wc.info()
    print "Logged in as: ", info['first_name'], info['last_name'], '(%s)' % info.get('username', '')
    print "Level: ", info['level_no'], "Race:", info['race'], "Group:", info['group'], "Points:", info['points']


def run_new(args):
    https, server, port, path = get_instance(args)
    wc = WousoClient(server=server, port=port, path=path, https=https)

    wc.authorize()
    print "Access token: '%s'" % wc.access_token
    print wc.info()
    print wc.notifications()

def run_existing(args):
    string = args[0]
    https, server, port, path = get_instance(args[1:])
    wc = WousoClient(server=server, port=port, access_token=string, path=path, https=https)

    print wc.info()
    print wc.notifications()


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Wouso API client')
    parser.add_argument('--config')
    parser.add_argument('--show_config', action='store_true')
    parser.add_argument('--authorize', nargs='+', help='Attempt OAuth authorization. Must provide server, port and path, such as: localhost:8000/2013')
    parser.add_argument('legacy', nargs='*')
    args = parser.parse_args()
    config = get_config()
    if args.show_config:
        if not config:
            print "No config file found at %s" % CONFIG_FILE
            sys.exit(-1)
        else:
            print "Available configurations:"
            for s in config.sections():
                print " ", s
            print "\nUse `%s --config [name]` to run with a specific config"
        sys.exit(0)

    if args.config:
        if not config.has_section(args.config):
            print "No such configuration: %s" % args.config
            sys.exit(-2)
        else:
            options = config.options(args.config)
            get_info(dict([(o, config.get(args.config, o)) for o in options]))
            sys.exit(0)

    if args.authorize:
        run_new(args.authorize)
        sys.exit(0)
    if len(sys.argv) >= 2:
        if sys.argv[1] == 'help':
            print 'Usage %s [token <string>] or <server> <port>' % sys.argv[0]
            sys.exit(0)
    if len(sys.argv) >= 3 and sys.argv[1] == 'token':
        run_existing(sys.argv[2:])
    else:
        run_new(sys.argv[1:])
