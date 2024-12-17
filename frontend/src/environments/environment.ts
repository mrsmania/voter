class Environment {
  production = false;
  private hostname = window.location.hostname;
  backendUrl = `http://${this.hostname}:8080`;
  apiUrl = `${this.backendUrl}/api`;
  url = `http://${this.hostname}`;
}

export const environment = new Environment();
