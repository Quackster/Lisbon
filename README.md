# Lisbon

Lisbon is a Habbo Hotel emulator that is designed to fully emulate the v26 version from 2008 era. 

## Linux install

The Linux installer deploys Lisbon with Java 17, MariaDB, Nginx, the emulator service, the web service, database imports, and systemd units.

Run it on a systemd-based Linux server:

```bash
sudo ./install-linux.sh --host example.com
```

Useful options:

```bash
sudo ./install-linux.sh \
  --host example.com \
  --db-name lisbon \
  --db-user lisbon \
  --game-port 30000 \
  --mus-port 12322 \
  --web-backend-port 8080
```

After installation:

```bash
systemctl status lisbon-server lisbon-web nginx mariadb --no-pager
journalctl -u lisbon-server -f
journalctl -u lisbon-web -f
```

## Quick VM test

The quickest realistic test is a disposable Ubuntu VM with Multipass.

From Windows PowerShell:

```powershell
multipass launch 24.04 --name lisbon-test --cpus 2 --memory 4G --disk 20G
multipass mount C:\SourceControl\Lisbon lisbon-test:/src/Lisbon
multipass shell lisbon-test
```

Inside the VM:

```bash
cd /src/Lisbon
chmod +x install-linux.sh
sudo ./install-linux.sh --host "$(hostname -I | awk '{print $1}')"
```

Check the services:

```bash
systemctl status lisbon-server lisbon-web nginx mariadb --no-pager
curl -I http://localhost/
journalctl -u lisbon-server -n 100 --no-pager
journalctl -u lisbon-web -n 100 --no-pager
```

From Windows, get the VM IP:

```powershell
multipass info lisbon-test
```

Open `http://<vm-ip>/` in a browser.

Clean up the VM when finished:

```powershell
multipass delete lisbon-test
multipass purge
```
