# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  config.vm.hostname = "devops-vm"

  # Forward ports so you can access tools from host
  config.vm.network "forwarded_port", guest: 8080, host: 8080   # Jenkins
  config.vm.network "forwarded_port", guest: 9000, host: 9000   # SonarQube
  config.vm.network "forwarded_port", guest: 8081, host: 8081   # Nexus
  config.vm.network "forwarded_port", guest: 27017, host: 27017 # MongoDB
  config.vm.network "forwarded_port", guest: 3000, host: 3000   # Grafana (optional)
  config.vm.network "forwarded_port", guest: 9090, host: 9090   # Prometheus (optional)

  # Optional: Assign more memory and CPUs
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "4096"
    vb.cpus = "2"
  end

  # Provisioning: Install dependencies and start services
  config.vm.provision "shell", inline: <<-SHELL
    # Update system
    sudo apt update && sudo apt upgrade -y

    # Install utilities
    sudo apt install -y git curl wget unzip maven openjdk-17-jdk

    # Install Docker
    sudo apt install -y docker.io

    # Install Docker Compose
    sudo apt install -y docker-compose

    # Add vagrant user to Docker group
    sudo usermod -aG docker vagrant

    # Create directory for project
    mkdir -p /home/vagrant/adoption-project
    cd /home/vagrant/adoption-project

    # Clone your GitHub repo
    git clone https://github.com/sarah-ammar1/adoption-project.git  .
    
    # Start all services via Docker Compose
    sudo docker-compose up -d

    echo "✅ VM setup complete!"
    echo "🔗 Access:"
    echo " - Jenkins     : http://localhost:8080"
    echo " - SonarQube   : http://localhost:9000"
    echo " - Nexus       : http://localhost:8081"
    echo " - MongoDB     : localhost:27017"
    echo " - Grafana     : http://localhost:3000 (if enabled)"
    echo " - Prometheus  : http://localhost:9090 (if enabled)"
  SHELL
end

